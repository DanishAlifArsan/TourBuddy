const express = require('express');
const router = express.Router();
const apiController = require('../controllers/apiController');
const verifyToken = require('../middlewares/verifyToken');

router.get('/destination', verifyToken.verifyToken, apiController.getDestinationByCoordinates);
router.get('/review', verifyToken.verifyToken, apiController.getReviewsByDestinationId);
router.post('/addreview', verifyToken.verifyToken, apiController.addReview);

module.exports = router;
