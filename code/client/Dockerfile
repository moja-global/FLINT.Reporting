FROM node@sha256:5bb95b92722e77f06ceff54ab21b1a5211c473269947ea65e48ed140fa8b4572 AS builder

# Set working directory
WORKDIR /usr/src/app

# Copy all files from current directory to working dir in image
COPY . ./

# install node modules and build assets
RUN npm install -g @angular/cli
RUN npm install
RUN ng build

# nginx state for serving content
FROM nginx@sha256:eb05700fe7baa6890b74278e39b66b2ed1326831f9ec3ed4bdc6361a4ac2f333

# Set working directory to nginx asset directory
WORKDIR /usr/share/nginx/html

# Remove default nginx static assets
RUN rm -rf ./*

# Copy static assets from builder stage
COPY --from=builder /usr/src/app/dist/client .

COPY default.conf /etc/nginx/conf.d/

# Containers run nginx with global directives and daemon off
ENTRYPOINT ["nginx", "-g", "daemon off;"]
