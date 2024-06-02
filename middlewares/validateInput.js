const { body, validationResult } = require('express-validator');

const validateSignup = [
    body('username').notEmpty().withMessage('Username is required'),
    body('email').isEmail().withMessage('Invalid email address'),
    body('password').isLength({ min: 8 }).withMessage('Password must be at least 8 characters long')
                     .matches(/[A-Z]/).withMessage('Password must contain at least one uppercase letter')
                     .matches(/[0-9]/).withMessage('Password must contain at least one number'),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({ error: true, message: errors.array() });
        }
        next();
    }
];

const validateLogin = [
    body('email').isEmail().withMessage('Invalid email address'),
    body('password').notEmpty().withMessage('Password is required'),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({ error: true, message: errors.array() });
        }
        next();
    }
];

module.exports = { validateSignup, validateLogin };
