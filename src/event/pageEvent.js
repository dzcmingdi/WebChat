import $ from 'jquery';
var uploadToServer;
const setUploadToServer = (call) => {
    uploadToServer = call;
};



window.onbeforeunload = (e)=>{
    uploadToServer();
};



export { setUploadToServer }