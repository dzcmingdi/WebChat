class MessageHandler {
    constructor() {
        this.handlers = [];
    }
    addHandler(handler) {this.handlers.push(handler); return this;}
    invoke(args,message) {
        let messageVar = {...message};
        this.handlers.forEach((handler,handlerIndex)=>{
            handler(args,messageVar);
        });
    }
    addFilter(filter){
        this.handlers.push(filter);
    }
}

export {
    MessageHandler
}