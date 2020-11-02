var nodemailer = require('nodemailer');
var info = require('./db_loginfo')
module.exports.send_mail = (req,res)=>{
    let email = req.body.email;
    let id = info.mail_config.id;
    let pw = info.mail_config.pw;
    var transporter = nodemailer.createTransport({ // 매일전송객체생성
        service:'gmail',
        auth:{
            user:id, //메일전송자 이메일 주소
            pass:pw //메일 전송자 비밀번호
        }
    });
    var temp_pw = make_secure(6);
    var mailOption = { //매일전송정보
        from:'hsgyu8974@gmail.com',
        to:email, //보내고자 하는 메일 
        subject:'임시비밀번호 확인메일입니다', //제목
        text:temp_pw
    };
    transporter.sendMail(mailOption).then((info)=>{
       
            console.log('Email sent:'+info.response);
            res.send("메일전송완료")
        
    }).catch((err)=>{
        if(err) console.log(err);
    })

}
//난수 생성 함수 (실제 프로젝트 사용시 수정)
function make_secure(n)
{
    var rand_num=""
    for(var i=0; i<n; i++)
    {
        rand_num+=make_rand(0,9);
    }
    return rand_num;

}
function make_rand(min,max)
{
    return parseInt(Math.random()*(max-min+1))+min;
}