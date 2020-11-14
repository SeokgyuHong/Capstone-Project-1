const {pool} = require('../secret_info/db_connect');
const format_check = require('../format_check');



module.exports.account_information_check = (req,res)=>{
    let email_address = req.body.email_address;
    if(!format_check.e_mail_check(email_address)) //이메일 양식 체크
    {
        res.send({'key':1}) //이메일 양식 에러
        return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`select sign_in_status from User
        Where email_address = '${email_address}'`).then((data)=>{
            if(data[0]['sign_in_status']=='1')//로그인상태인지 체크하기
            {
                conn.query(`select email_address,inst_name,inst_address,phone_number
                from User where email_address = '${email_address}'`).then((data)=>{

                    res.send({'key':2,'information':data[0]}); //계정 정보 전달
                }).catch((err)=>{
                    console.log(err)
                    res.send({'key':0,'err_code':err.code});
                })
            }
            else{
                res.send({'key':4}) //로그인상태가아님
            }
            
        }).catch((err)=>{
            console.log(err)
            res.send({'key':3,'err_code':err.code}); //잘못된 계정정보 전달
        })
        conn.release();
    }).catch((err)=>{
        console.log(err)
        res.send({'key':0,'err_code':err.code});
    })
    
    
}