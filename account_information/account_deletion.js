const {pool} = require('../secret_info/db_connect');
const format_check = require('../format_check');



module.exports.account_deletion = (req,res)=>{
    let email_address = req.body.email_address;

    if(!format_check.e_mail_check(email_address)) //이메일 양식 체크
    {
        res.send({'key':1}) //이메일 양식 에러
        return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`select sign_in_status from User where email_address='${email_address}'`).then((data)=>{
            if(data[0]['sign_in_status']=='1') //로그인되어있는상태이면
            {
                conn.query(`delete from User where email_address='${email_address}'`).then((data)=>{
                    res.send({'key':3}) //계정삭제완료
                }).catch((err)=>{
                    console.log(err)
                    res.send({'key':4,'err_code':err.code}) //계정삭제실패
                })
            }
            else
            {
                res.send({'key':2}) //로그인이 안되어있는상태 (오류)
            }
        }).catch((err)=>{
            console.log(err)
            res.send({'key':0,'err_code':err.code}); //시스템 에러
        })
        conn.release();
    }).catch((err)=>{
        console.log(err)
        res.send({'key':0,'err_code':err.code}); //시스템에러
    })


    

}