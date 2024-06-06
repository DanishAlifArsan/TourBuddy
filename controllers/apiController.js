const destinations = [
    {
        destination_id: "1",
        destination_name: "Jakarta",
        description: "Capital city of Indonesia",
        city: "Jakarta",
        lat: -6.200000,
        lon: 106.816666,
        rating: 5,
        rating_count: 1000,
        photoUrl: "https://example.com/photo.jpg",
        url_maps: "https://maps.google.com/?q=-6.200000,106.816666",
        reviews: [
            {
                reviewer_name: "John Doe",
                review: "Great place!",
                rating: 4,
                createdAt: "2024-06-06"
            }
        ]
    },
    // Add more destinations as needed
];

const getDestinationByCoordinates = (req, res) => {
    const lat = parseFloat(req.query.lat);
    const lon = parseFloat(req.query.lon);

    if (isNaN(lat) || isNaN(lon)) {
        return res.status(400).json({
            error: true,
            message: "Invalid latitude or longitude"
        });
    }

    const destination = destinations.find(dest => dest.lat === lat && dest.lon === lon);

    if (!destination) {
        return res.status(404).json({
            error: true,
            message: "Destination not found"
        });
    }

    return res.status(200).json({
        error: false,
        message: "Destination fetched successfully",
        listDestinations: [destination]
    });
};

const getReviewsByDestinationId = (req, res) => {
    const destination_id = req.body.destination_id;

    const destination = destinations.find(dest => dest.destination_id === destination_id);

    if (!destination) {
        return res.status(404).json({
            error: true,
            message: "Destination not found"
        });
    }

    return res.status(200).json({
        error: false,
        message: "Reviews fetched successfully",
        listReviews: destination.reviews
    });
};

const addReview = (req, res) => {
    const { destination_id, review, rating } = req.body;

    const destination = destinations.find(dest => dest.destination_id === destination_id);

    if (!destination) {
        return res.status(404).json({
            error: true,
            message: "Destination not found"
        });
    }

    const newReview = {
        reviewer_name: "Anonymous",
        review: review,
        rating: rating,
        createdAt: new Date().toISOString()
    };

    destination.reviews.push(newReview);

    return res.status(201).json({
        error: false,
        message: "Review added successfully"
    });
};

module.exports = {
    getDestinationByCoordinates,
    getReviewsByDestinationId,
    addReview
};
