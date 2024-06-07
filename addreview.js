exports.addReview = (req, res) => {
    const { destination_id, review, rating } = req.body;
    const username = req.user ? req.user.username : "Anonymous"; // Asumsinya nama pengguna disimpan dalam req.user setelah otentikasi

    // ratingnys integer
    const parsedRating = parseInt(rating, 10);

    if (isNaN(parsedRating) || parsedRating < 1 || parsedRating > 5) {
        return res.status(400).json({
            error: true,
            message: "Rating must be an integer between 1 and 5"
        });
    }

    const destination = destinations.find(dest => dest.destination_id === destination_id);

    if (!destination) {
        return res.status(404).json({
            error: true,
            message: "Destination not found"
        });
    }

    const newReview = {
        reviewer_name: username,
        review: review,
        rating: parsedRating,
        createdAt: new Date().toISOString()
    };

    destination.reviews.push(newReview);

    return res.status(201).json({
        error: false,
        message: "Review added successfully"
    });
};
