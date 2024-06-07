const express = require('express');
const router = express.Router();
const apiController = require('../controllers/apiController');
const authMiddleware = require('../middlewares/authMiddleware');
const { validateInput } = require('../middlewares/validateInput');

router.post('/review', authMiddleware, validateInput, apiController.addReview); // Tambahkan authMiddleware ke rute ini
router.get('/reviews', apiController.getReviewsByDestinationId); // Rute lain jika diperlukan

module.exports = router;
