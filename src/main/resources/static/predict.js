document.getElementById('fileInput').addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('selectedImage').src = e.target.result;
            document.getElementById('selectedImage').style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
});

document.getElementById('predictForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData();
    const fileInput = document.getElementById('fileInput');
    formData.append('file', fileInput.files[0]);

    fetch('/predict', {
        method: 'POST',
        body: formData
    })
    .then(response => response.text())
    .then(result => {
        const mapping = {
            0: { name: 'phlox', description: 'A genus of 67 species of perennial and annual plants.' },
            1: { name: 'rose', description: 'A woody perennial flowering plant of the genus Rosa.' },
            2: { name: 'calendula', description: 'A genus of about 15–20 species of annual and perennial herbaceous plants.' },
            3: { name: 'iris', description: 'A genus of 260–300 species of flowering plants.' },
            4: { name: 'leucanthemum maximum (Shasta daisy)', description: 'A perennial plant of the family Asteraceae.' },
            5: { name: 'campanula (bellflower)', description: 'A genus of around 500 species of annual, biennial, and perennial plants.' },
            6: { name: 'viola', description: 'A genus of flowering plants in the violet family Violaceae.' },
            7: { name: 'rudbeckia laciniata (Goldquelle)', description: 'A species of flowering plant in the family Asteraceae.' },
            8: { name: 'peony', description: 'A flowering plant in the genus Paeonia, the only genus in the family Paeoniaceae.' },
            9: { name: 'aquilegia', description: 'A genus of about 60–70 species of perennial plants.' }
        };
        const prediction = mapping[result] || { name: 'Unknown', description: 'No description available.' };
        document.getElementById('predictionResult').textContent = `${prediction.name}: ${prediction.description}`;
    })
    .catch(error => console.error('Error:', error));
});