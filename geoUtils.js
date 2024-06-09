// utils/geoUtils.js

const haversineDistance = (coords1, coords2) => {
    function toRad(x) {
        return x * Math.PI / 180;
    }

    const lat1 = coords1.lat;
    const lon1 = coords1.lon;
    const lat2 = coords2.lat;
    const lon2 = coords2.lon;

    const R = 6371; // Radius of the Earth in kilometers
    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
              Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = R * c;

    return distance;
};

module.exports = haversineDistance;
