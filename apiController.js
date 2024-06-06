exports.getDestinations = (req, res) => {
    // Contoh data respons
    const destinations = [
        {
            "destination_id": "story-FvU4u0Vp2S3PMsFg",
            "destination_name": "Dimas",
            "description": "Lorem Ipsum",
            "city": "Malang",
            "rating": 47,
            "rating_count": 500,
            "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
            "url_maps": "https://maps.google.com/..."
        }
    ];
    res.json({
        error: false,
        message: "Destinations fetched successfully",
        listDestinations: destinations
    });
};

exports.getReviews = (req, res) => {
    // Contoh data respons
    const reviews = [
        {
            "reviewer_name": "Dimas",
            "review": "Lorem Ipsum",
            "rating": 4,
            "createdAt": "22 Juni 2024"
        }
    ];
    res.json({
        error: false,
        message: "Reviews fetched successfully",
        listReviews: reviews
    });
};

exports.addReview = (req, res) => {
    const { review, rating, destination_id } = req.body;
    // Logika untuk menambahkan review baru
    res.json({
        error: false,
        message: "Review added successfully"
    });
};
