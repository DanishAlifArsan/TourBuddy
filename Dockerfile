# Use the official Node.js 14 image
FROM node:14

# Set working directory
WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy all other source code to the working directory
COPY . .

# Expose port 8080
EXPOSE 8080

# Command to run the application
CMD ["node", "app.js"]
