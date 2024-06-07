// middlewares/authMiddleware.js
const jwt = require('jsonwebtoken');
const User = require('../models/User'); // Sesuaikan dengan model User 

const authMiddleware = async (req, res, next) => {
    const token = req.header('Authorization')?.replace('Bearer ', '');

    if (!token) {
        return res.status(401).json({ error: 'No token provided, please authenticate.' });
    }

    try {
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        const user = await User.findById(decoded.id);

        if (!user) {
            return res.status(401).json({ error: 'Invalid token, user not found.' });
        }

        req.user = user;
        next();
    } catch (error) {
        res.status(401).json({ error: 'Please authenticate.' });
    }
};

module.exports = authMiddleware;
