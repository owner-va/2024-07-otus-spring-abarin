const TerserPlugin = require("terser-webpack-plugin");
const HtmlWebpackPlugin = require('html-webpack-plugin')
const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: './src/index.js',
    mode: "development",
    output: {
        path: path.resolve(__dirname, '../target/classes/public/'),
        filename: 'bundle.min.js',
        libraryTarget: 'umd'
    },

    module: {
        rules: [
            {
                test: /.js$/,
                exclude: /(node_modules|bower_components|build)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", '@babel/preset-react']
                    }
                }
            },
            {
                test: /.css$/,
                use: ['style-loader', 'css-loader'],
            }
        ]
    },

    optimization: {
        minimize: true,
        minimizer: [
            new TerserPlugin({
                extractComments: true,
            }),
        ],
    },

    plugins: [
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: './src/index.html'
        })
    ],


    devServer: {
        port: 3000,
        compress: true,
        hot: true,
        open: true,
        proxy: {
            '*': {
                target: 'http://localhost:8080',
                secure: false,
                changeOrigin: true
            }
        }
    }
};