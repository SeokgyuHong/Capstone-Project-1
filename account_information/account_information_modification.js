const {pool} = require('../db_connect');
const format_check = require('../format_check');



module.exports.account_information_modification = (req,res)=>{
    let email_address = req.body.email_address;
    let inst_name = req.body.inst_name;
    let phone_number = req.body.phone_number;
    let inst_address = req.body.inst_address;

    if(!format_check.e_mail_check(email_address)) //이메일 양식 체크
    {
        res.send({'key':1}) //이메일 양식 에러
        return;
    }
    if(!format_check.phone_check(phone_number)) //휴대폰 번호 양식 체크
    {
        res.send({'key':2}) //휴대폰번호 양식 에러
        return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`update User set inst_name='${inst_name}',phone_number='${phone_number}',inst_address='${inst_address}'
        where email_address='${email_address}'`).then((data)=>{
            res.send({'key':3})// 계정 정보 업데이트 성공
        }).catch((err)=>{
            console.log(err)
            res.send({'key':4,'err_code':err.code}) // 계정정보 업데이트 실패 
        })
        conn.release();
    }).catch((err)=>{
        console.log(err)
        res.send({'key':0,'err_code':err.code});
    })
    
    
}