var admin= require('firebase-admin');
var serviceAccount = require('/home/seokgyuhong/nodejs_practice/capstone_project1/fcm_key/my-application-eb059-firebase-adminsdk-2857l-84af43d294.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
    });

module.exports.send_fcm = (req,res)=>{
  let fcm_target_token = req.body.token; //클라이언트로부터 토큰 정보 수신
    var fcm_message ={ //토큰메시지 객체 
        
        notification:{
            title:'Fcm',
            body:'Fcm test'
        },
      
        token:fcm_target_token,
        
    };
    //console.log(fcm_target_token)

    admin.messaging().send(fcm_message).then((response) => { //토큰 메시지 전달 
    console.log('Successfully sent message:', response);
    res.sned("토큰메시지 전송 완료");
  }).catch((error) => {
    console.log('Error sending message:', error);
  });
    
}