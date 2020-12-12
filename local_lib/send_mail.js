var nodemailer = require('nodemailer');
var info = require('../secret_info/db_loginfo')
var mail_info = info.mail_config();
module.exports.send_mail = (email)=>{

    let id = mail_info.id;
    var temp_pw = make_secure(6);


    return {'mailOption':{ //매일전송정보
        from:id,
        to:email, //보내고자 하는 메일 
        subject:'임시비밀번호 확인메일입니다', //제목
        text:`본 어플리케이션으로 돌아가 해당 임시 번호를 입력해주세요. ${temp_pw}`},
        'temp_pw':temp_pw};
    

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