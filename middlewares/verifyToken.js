const jwt = require('jsonwebtoken');

exports.verifyToken = (req, res, next) => {
    const token = req.headers['authorization'];
    
    if (!token) {
        return res.status(403).json({ error: true, message: 'Token tidak tersedia' });
    }

    // Ekstrak token dari header `Authorization: Bearer <token>`
    const bearerToken = token.split(' ')[1];

    try {
        // Verifikasi token
        const decoded = jwt.verify(bearerToken, process.env.JWT_SECRET); // Pastikan untuk menggunakan secret yang sama saat menghasilkan token
        req.user = decoded;
        next(); // Lanjutkan ke handler berikutnya
    } catch (error) {
        return res.status(403).json({ error: true, message: 'Token tidak valid' });
    }
};