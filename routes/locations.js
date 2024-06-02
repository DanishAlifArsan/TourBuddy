const express = require('express');
const router = express.Router();
const axios = require('axios');

router.post('/search', async (req, res) => {
    const { place } = req.body;
    const apiKey = process.env.GOOGLE_MAPS_API_KEY;
    const url = `https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=${encodeURIComponent(place)}&inputtype=textquery&fields=geometry&key=${apiKey}`;

    try {
        const response = await axios.get(url);
        const { candidates } = response.data;
        if (candidates.length > 0) {
            const { lat, lng } = candidates[0].geometry.location;
            res.json({ latitude: lat, longitude: lng });
        } else {
            res.status(404).json({ error: 'Tempat tidak ditemukan' });
        }
    } catch (error) {
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

module.exports = router;
