const {pool} = require('../secret_info/db_connect');
const format_check = require('../local_lib/format_check');
const info = require('../secret_info/db_loginfo');

module.exports.social_sign_up = (req,res)=>{ //추가정보입력
    let email_address = req.body.email_address;
    let phone_number = req.body.phone_number;
    let inst_name = req.body.inst_name;
    let inst_address = req.body.inst_address;
    let user_type = req.body.user_type;

    

    if(!format_check.e_mail_check(email_address))
    {
    console.log(email_address);
    console.log('social_extra info_update_email_check error')
    res.send({'key':1}) //이메일 양식 체크
    return;
    }
    if(!format_check.phone_check(phone_number))
    {
        console.log('social_extrainfo_update_phone_check error');
        res.send({'key':2}) //폰번호 양식에러
        return;

    }
    pool.getConnection().then((conn)=>{
        conn.query(`insert into User (email_address,phone_number,inst_name,inst_address,user_type,sign_in_status)
        values('${email_address}','${phone_number}','${inst_name}','${inst_address}','${user_type}','0')`).then((data)=>{
            res.send({'key':3})//회원가입성공
        }).catch((err)=>{
            console.log(err)
            console.log('소셜계정 회원가입중 에러발생')
            res.send({'key':4}) //회원가입실패
        })
        conn.release();
    }).catch((err)=>{
        console.log(err)
        console.log('소셜계정 회원가입중 시스템 에러')
        res.send({'key':0,'err_code':err.code});
    })
}