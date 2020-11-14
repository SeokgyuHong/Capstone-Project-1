
const {pool} = require('../db_connect');
const format_check = require('../format_check');




module.exports.social_sign_in =(req,res)=>{
    let email_address = req.body.email_address;
    if(!format_check.e_mail_check(email_address)) //이메일 양식 체크
    {
        res.send({'key':1}) //이메일 양식 에러
        return;
    }
    pool.getConnection().then((conn)=>{
        conn.query(`select email_address from User where email_address='${email_address}'`).then((data)=>{
            if(data[0]===undefined)
            {
                res.send({'key':2}) //등록되지않은 이메일입니다.
                //return;
            }
            else
            {
                conn.query(`update User set sign_in_status = '1'
                where email_address='${email_address}'`).then((data)=>{
                    res.send({'key':3})//로그인성공
                }).catch((err)=>{
                    console.log(err)
                    res.send({'key':0,'err_code':err.code}) //0 시스템에러
                })
            }
        }).catch((err)=>{
            console.log(err)
            res.send({'key':0,'err_code':err.code}); //0 시스템에러
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code);
        res.send({'key':0,'err_code':err.code}); //0 시스템에러
    })
}