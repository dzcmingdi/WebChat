const path = require("path");
const idea_path = "D:\\IdeaProjects\\WebChatServer\\src\\main\\webapp\\static\\js";
const local_path = path.resolve(__dirname,"dist/js/");
module.exports = {
    entry:{
        index:'./src/index.js',
        test:'./src/test/test.js',
        login:'./src/user/login.js',
        register:'./src/user/register.js'
    },
    mode:'development',
    output:{
        filename:"[name].js",
        path:idea_path,
    },  
    module:{
        rules:[
            {
                test:/\.js$/,
                exclude:/node_modules/,
                use:{
                    loader:'babel-loader',
                    options:{
                        presets:['@babel/preset-react'],
                        plugins:[
                           
                            ["@babel/plugin-proposal-decorators", { "legacy": true }],
                            ["@babel/plugin-proposal-class-properties",{"legacy":true}],
                            "transform-class-properties"
                        ]
                    }
                }
            }
        ]
    },
    watch:true,
    watchOptions:{
        poll:1000,
        aggregateTimeout:500,
        ignored:/node_modules/
    }
};