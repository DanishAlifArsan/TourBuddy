require('dotenv').config();
const express = require('express');
const app = express();
const path = require('path');
const locationsRouter = require('./routes/locations');

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'public')));
app.use('/api/locations', locationsRouter);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
