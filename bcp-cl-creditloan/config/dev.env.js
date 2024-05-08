'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  NODE_CONFIG: '"dev"',
  BASE_URL: '"http://localhost:8086/json"'
})