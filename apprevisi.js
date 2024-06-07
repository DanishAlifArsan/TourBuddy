const express = require('express');
const bodyParser = require('body-parser');
const dotenv = require('dotenv');
const apiRoutes = require('./routes/apiRoutes');
const authMiddleware = require('./middlewares/authMiddleware');

dotenv.config();

const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Gunakan rute API
app.use('/api', apiRoutes);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
