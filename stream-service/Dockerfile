FROM node:14 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run

FROM node:alpine as main
COPY --from=build /app /
EXPOSE 8085
CMD ["app.js"]