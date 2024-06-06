// app.js

const express = require('express');
const app = express();
const apiRoutes = require('./routes/apiRoutes');
const authRoutes = require('./routes/authRoutes'); // Existing route

app.use(express.json());

// Use the new API routes
app.use('/api', apiRoutes);

// Use the existing auth routes
app.use('/auth', authRoutes);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
