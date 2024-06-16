const haversine = require('haversine-distance');

const calculateDistance = (loc1, loc2) => {
  return haversine(loc1, loc2) / 1000; 
};

module.exports = calculateDistance;
