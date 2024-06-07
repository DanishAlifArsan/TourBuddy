const { firestore } = require('firebase-admin');

const getReviewsByDestinationId = async (req, res) => {
    const destination_id = req.query.destination_id;

    try {
        const destinationDoc = await firestore().collection('destinations').doc(destination_id).get();
        
        if (!destinationDoc.exists) {
            return res.status(404).json({
                error: true,
                message: "Destination not found"
            });
        }

        const destination = destinationDoc.data();
        
        return res.status(200).json({
            error: false,
            message: "Reviews fetched successfully",
            listReviews: destination.reviews
        });
    } catch (error) {
        console.error("Error fetching reviews from Firestore:", error);
        return res.status(500).json({
            error: true,
            message: "Failed to fetch reviews from Firestore"
        });
    }
};

module.exports = {
    getReviewsByDestinationId
};
