const { firestore, db, admin} = require('../config/firebaseConfig');
// const { Storage } = require('@google-cloud/storage');
// const csv = require('csv-parser');

async function updateDestinations() {
  try {
    const destinationsRef = firestore.collection('destinations');
    const snapshot = await destinationsRef.get();

    const batch = firestore.batch();

    snapshot.forEach((doc) => {
      const destinationData = doc.data();
      const destinationId = doc.id;

      // Update photoUrl and url_maps
      const photoUrl = `https://storage.cloud.google.com/tourbuddy-dataset/assets-gambar/${destinationData.destination_id}%20${destinationData.destination_name}.jpg`;
      const mapsUrl = `https://maps.google.com/?q=${destinationData.lat},${destinationData.lon}`;

      batch.update(destinationsRef.doc(destinationId), {
        photoUrl: photoUrl,
        url_maps: mapsUrl
      });
    });

    // Commit the batch
    await batch.commit();

    console.log('Update selesai.');
  } catch (error) {
    console.error('Terjadi kesalahan:', error);
  }
}

module.exports = { updateDestinations };
