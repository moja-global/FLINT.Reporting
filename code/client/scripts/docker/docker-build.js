const version = require('../version.js');
const sh = require('shelljs');

const imageName = 'client';

sh.exec(`docker build -t ${imageName}:latest -t ${imageName}:${version} .`);

