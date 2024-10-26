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
            0: 'phlox',
            1: 'rose',
            2: 'calendula',
            3: 'iris',
            4: 'leucanthemum maximum (Shasta daisy)',
            5: 'campanula (bellflower)',
            6: 'viola',
            7: 'rudbeckia laciniata (Goldquelle)',
            8: 'peony',
            9: 'aquilegia'
        };
        const prediction = mapping[result] || 'Unknown';
        document.getElementById('predictionResult').textContent = prediction;
    })
    .catch(error => console.error('Error:', error));
});