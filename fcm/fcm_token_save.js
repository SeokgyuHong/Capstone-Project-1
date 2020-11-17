const {pool} = require('../secret_info/db_connect');
const format_check = require('../format_check');


module.exports.fcm_token_save = (req,res)=>{
    let email_address = req.body.email_address;
    let fcm_token = req.body.fcm_token;

    if(!format_check.e_mail_check(email_address)) //이메일 양식 체크
    {
        res.send({'key':1}) //이메일 양식 에러
        return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`select sign_in_status from User
        where email_address='${email_address}'`).then((data)=>{
            if(data[0]['sign_in_status']=='1')
            {
                conn.query(`update User set token='${fcm_token}'
                where email_address='${email_address}'`).then((data)=>{
                    res.send({'key':2})//token저장 성공
                }).catch((err)=>{
                    console.log('토큰저장실패'+err.code)
                    res.send({'key':3})//token저장 실패
                })
            }
            else{
                console.log('토큰 저장시 로그인상태에러')
                res.send({'key':4})//로그인상태가아님
            }

        }).catch((err)=>{
            console.log('토큰을 위한 데이터베이스 접근시 에러'+err.code)
            res.send({'key':0,'err_code':err.code});
        })
        conn.release();
    }).catch((err)=>{
        console.log('토큰을 위한 데이터 베이스 접근시 시스템 에러'+err.code);
        res.send({'key':0,'err_code':err.code});
    })
    

}