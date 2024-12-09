FROM node:20.18.0 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

FROM node:20.18.0
WORKDIR /app
COPY --from=build /app ./
EXPOSE 3000
CMD ["npm", "start"]
