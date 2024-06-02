const express = require('express');
const { signUp, login } = require('../controllers/authController');
const { validateSignup, validateLogin } = require('../middlewares/validateInput');
const router = express.Router();

router.post('/signup', validateSignup, signUp);
router.post('/login', validateLogin, login);

module.exports = router;
