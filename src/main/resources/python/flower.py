import torch
from torchvision import models, transforms, datasets
from torch import nn, optim
from torch.utils.data import DataLoader

# Define the number of classes
num_classes = 10

# Load the pre-trained model
model = models.resnet18(pretrained=True)

# Modify the final layer to match the number of classes
model.fc = nn.Linear(model.fc.in_features, num_classes)

# Define the loss function and optimizer
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=0.001)

# Define the image transformations
transform = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

# Load your dataset
train_dataset = datasets.ImageFolder('c:/images', transform=transform)
train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)

# Training loop
model.train()
for epoch in range(10):  # Number of epochs
    running_loss = 0.0
    for images, labels in train_loader:
        optimizer.zero_grad()
        outputs = model(images)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()
        running_loss += loss.item()
    print(f"Epoch {epoch+1}, Loss: {running_loss/len(train_loader)}")

# Save the fine-tuned model
torch.save(model.state_dict(), 'fine_tuned_resnet18.pth')

# Save the model in TorchScript format
model_scripted = torch.jit.script(model)
model_scripted.save('fine_tuned_resnet18_scripted.pt')

# Example usage
image_path = 'C:/data/flower_images/flower_images/0023.png'
predicted_class = predict(image_path)
print(f"Predicted class: {predicted_class}")