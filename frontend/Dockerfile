#Compilazione dell'app Angular
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration=production

#Server Web leggero per distribuire i file statici
FROM nginx:alpine
COPY --from=build /app/dist/<nome-tua-app-angular>/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]