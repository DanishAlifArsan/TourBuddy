// routes/apiRoutes.js

const express = require('express');
const router = express.Router();
const apiController = require('../controllers/apiController');
const authMiddleware = require('../middlewares/authMiddleware');

router.get('/destination', authMiddleware.verifyToken, apiController.getDestinationByCoordinates);
router.get('/review', authMiddleware.verifyToken, apiController.getReviewsByDestinationId);
router.post('/addreview', authMiddleware.verifyToken, apiController.addReview);

module.exports = router;
