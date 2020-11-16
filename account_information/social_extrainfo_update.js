const {pool} = require('../secret_info/db_connect');
const format_check = require('../format_check');

//해야함 social_sign_in 없애고 이거 써야함

module.exports.social_extrainfo_update = (req,res)=>{ //추가정보입력

    if(!format_check.e_mail_check(email_address))
    {
    console.log('social_extrainfo_update_email_check error')
    res.send({'key':1}) //이메일 양식 체크
    return;
    }
    if(!format_check.phone_check(phone_number))
    {
        console.log(console.log('social_extrainfo_update_email_check error'))
    }
    let email_address = req.body.email_address;
    let phone_number = req.body.phone_number;
    let inst_name = req.body.inst_name;
    let inst_address = req.body.inst_address;

    pool.getConnection().then((conn)=>{
        conn.query(``)
    })
}