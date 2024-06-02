const argon2 = require('argon2');
const { admin, db } = require('../config/firebaseConfig');
const generateToken = require('../utils/generateToken');

const signUp = async (req, res) => {
    const { username, email, password } = req.body;

    try {
        console.log('Checking if user already exists...');
        const userRef = db.collection('users').doc(email);
        const userDoc = await userRef.get();

        if (userDoc.exists) {
            console.log('User already exists.');
            return res.status(400).json({ error: true, message: 'Email already exists' });
        }

        console.log('Hashing password...');
        const hashedPassword = await argon2.hash(password);

        console.log('Creating user in Firebase Authentication...');
        const userRecord = await admin.auth().createUser({
            email: email,
            password: password,
            displayName: username,
        });

        console.log('Saving user to Firestore...');
        await userRef.set({
            username,
            email,
            password: hashedPassword,
            uid: userRecord.uid
        });

        const token = generateToken(email);
        console.log('User created successfully.');
        return res.status(201).json({
            error: false,
            message: 'User Created',
            username,
            token,
            id: email
        });
    } catch (error) {
        console.error('Error during sign up:', error);  // Logging error
        return res.status(500).json({ error: true, message: error.message });
    }
};

const login = async (req, res) => {
    const { email, password } = req.body;

    try {
        console.log('Checking if user exists...');
        const userRef = db.collection('users').doc(email);
        const userDoc = await userRef.get();

        if (!userDoc.exists) {
            console.log('User not found.');
            return res.status(400).json({ error: true, message: 'Invalid credentials' });
        }

        const userData = userDoc.data();
        console.log('Verifying password...');
        const isMatch = await argon2.verify(userData.password, password);

        if (!isMatch) {
            console.log('Password does not match.');
            return res.status(400).json({ error: true, message: 'Invalid credentials' });
        }

        const token = generateToken(email);
        console.log('User logged in successfully.');
        return res.status(200).json({
            error: false,
            message: 'success',
            loginResult: {
                userId: email,
                name: userData.username,
                token
            }
        });
    } catch (error) {
        console.error('Error during login:', error);  // Logging error
        return res.status(500).json({ error: true, message: error.message });
    }
};

module.exports = { signUp, login };
