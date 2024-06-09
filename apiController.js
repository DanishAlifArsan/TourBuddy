const { Firestore } = require('@google-cloud/firestore');
const { updateDestinations } = require('../services/importService');
const haversineDistance = require('../utils/geoUtils'); // Ini tambahnnya

// Inisialisasi Firestore
const firestore = new Firestore();

const getDestinationByCoordinates = async (req, res) => {
    const lat = parseFloat(req.query.lat);
    const lon = parseFloat(req.query.lon);

    if (isNaN(lat) || isNaN(lon)) {
        return res.status(400).json({
            error: true,
            message: "Invalid latitude or longitude"
        });
    }

    // Ambil data dari Firestore
    try {
        const querySnapshot = await firestore.collection('destinations')
            .where('lat', '==', lat)
            .where('lon', '==', lon)
            .get();

        if (querySnapshot.empty) {
            return res.status(404).json({
                error: true,
                message: "Destination not found"
            });
        }

        const destination = querySnapshot.docs[0].data();

        return res.status(200).json({
            error: false,
            message: "Destination fetched successfully",
            listDestinations: [destination]
        });
    } catch (error) {
        console.error("Error getting destination from Firestore:", error);
        return res.status(500).json({
            error: true,
            message: "Failed to fetch destination from Firestore"
        });
    }
};

const getReviewsByDestinationId = async (req, res) => {
    const destination_id = req.query.destination_id;

    if (!destination_id) {
        console.log("No destination_id in request body"); // Log jika destination_id tidak ada
        return res.status(400).json({
            error: true,
            message: "destination_id is required"
        });
    }

    console.log("Destination ID:", destination_id); // Log untuk memeriksa destination_id

    try {
        const destinationDocRef = firestore.collection('destinations').doc(destination_id);
        const destinationDoc = await destinationDocRef.get();
        
        if (!destinationDoc.exists) {
            console.log("Destination not found for ID:", destination_id); // Log jika dokumen tidak ditemukan
            return res.status(404).json({
                error: true,
                message: "Destination not found"
            });
        }

        const destination = destinationDoc.data(); // Mengakses data dokumen dengan benar
        console.log("Destination Data:", destination); // Log untuk memeriksa data dokumen

        if (!destination.reviews || destination.reviews.length === 0) {
            console.log("No reviews found in destination data"); // Log jika field reviews tidak ada
            return res.status(404).json({
                error: true,
                message: "No reviews found for this destination"
            });
        }

        // Menghitung rata-rata rating dari semua reviews
        let totalRating = 0;
        destination.reviews.forEach(review => {
            totalRating += review.rating;
        });
        const averageRating = totalRating / destination.reviews.length;

        // Memperbarui rating destinasi dengan rata-rata rating baru
        await destinationDocRef.update({
            rating: Math.round(averageRating) // Memastikan rating berupa bilangan bulat
        });

        return res.status(200).json({
            error: false,
            message: "Reviews fetched successfully",
            listReviews: destination.reviews,
            averageRating: Math.round(averageRating) // Menyertakan rata-rata rating dalam respons
        });
    } catch (error) {
        console.error("Error fetching reviews from Firestore:", error); // Log untuk error
        return res.status(500).json({
            error: true,
            message: "Failed to fetch reviews from Firestore"
        });
    }
};

const addReview = async (req, res) => {
    console.log("Memulai addReview, req.user:", req.user); // Tambahkan log untuk memeriksa req.user

    const { destination_id, review, rating } = req.body;

    // Validasi dan konversi rating
    const parsedRating = parseInt(rating, 10);
    if (isNaN(parsedRating) || parsedRating < 1 || parsedRating > 5) {
        return res.status(400).json({
            error: true,
            message: "Invalid rating. Rating should be an integer between 1 and 5."
        });
    }

    try {
        const destinationDoc = await firestore.collection('destinations').doc(destination_id).get();
        
        if (!destinationDoc.exists) {
            return res.status(404).json({
                error: true,
                message: "Destination not found"
            });
        }

        const destinationData = destinationDoc.data();

        // Validasi req.user dan req.user.name
        const reviewer_name = req.user && req.user.name ? req.user.name : "Anonymous";
        console.log("Reviewer Name:", reviewer_name); // Tambahkan log untuk memeriksa reviewer_name

        const newReview = {
            reviewer_name: reviewer_name,
            review: review,
            rating: parsedRating,
            createdAt: new Date().toISOString()
        };

        // Tambahkan review ke destinasi
        if (!destinationData.reviews) {
            destinationData.reviews = [];
        }
        destinationData.reviews.push(newReview);

        // Simpan data ke Firestore
        await firestore.collection('destinations').doc(destination_id).set(destinationData, { merge: true });
        
        console.log("Review successfully added to Firestore");
        return res.status(201).json({
            error: false,
            message: "Review added successfully"
        });
    } catch (error) {
        console.error("Error adding review to Firestore:", error);
        return res.status(500).json({
            error: true,
            message: "Failed to add review to Firestore"
        });
    }
};

const importDestinations = async (req, res) => {
    try {
        await updateDestinations();
        res.status(200).json({ error: false, message: "Data imported successfully" });
    } catch (error) {
        res.status(500).json({ error: true, message: "Failed to import data" });
    }
};

// Fungsi BARU untuk mendapatkan lokasi terdekat
const getNearbyLocations = async (req, res) => {
    const { latitude, longitude, radius } = req.query;
    
    if (!latitude || !longitude || !radius) {
        return res.status(400).json({ error: 'Latitude, longitude, and radius are required.' });
    }

    const userLocation = { lat: parseFloat(latitude), lon: parseFloat(longitude) };
    const radiusKm = parseFloat(radius);

    try {
        const querySnapshot = await firestore.collection('destinations').get();
        const locations = querySnapshot.docs.map(doc => doc.data());

        const nearbyLocations = locations.filter(location => {
            const locationCoords = { lat: location.latitude, lon: location.longitude };
            const distance = haversineDistance(userLocation, locationCoords);
            return distance <= radiusKm;
        });

        res.json(nearbyLocations);
    } catch (error) {
        console.error("Error getting nearby locations from Firestore:", error);
        return res.status(500).json({
            error: true,
            message: "Failed to fetch nearby locations from Firestore"
        });
    }
};

module.exports = {
    getDestinationByCoordinates,
    getReviewsByDestinationId,
    addReview,
    importDestinations,
    getNearbyLocations // ini Tambahannya
};
