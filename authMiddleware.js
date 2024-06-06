// middlewares/authMiddleware.js

exports.verifyToken = (req, res, next) => {
    const token = req.headers.authorization;

    if (!token || token !== 'valid-token') {
        return res.status(403).json({
            error: true,
            message: "Forbidden: Invalid token"
        });
    }

    next();
};
