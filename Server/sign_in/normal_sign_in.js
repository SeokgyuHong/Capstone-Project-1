//프로젝트 실사용 로그인 
//일반회원가입 구현 완료
//const {insert} = require('../db_sql.js');
const {pool} = require('../secret_info/db_connect');
const info = require('../secret_info/db_loginfo')
const {send_mail} = require('../local_lib/send_mail');
const format_check = require('../local_lib/format_check');
const nodemailer = require('nodemailer');
const crypto = require('crypto');


module.exports.normal_sign_in =(req,res)=>{
    let email_address = req.body.email_address;
    let password = req.body.password;
    let salt=''
    let hashed_password='' //디비에서 가져온 비밀번호
    let check_password='' //비교비밀번호
    let salt_num = db_loginfo.salt_num();
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
                conn.query(`select password,salt from User
                where email_address='${email_address}'`).then((data)=>{
                    salt = data[0]['salt']
                    hashed_password = data[0]['password']
                    crypto.pbkdf2(password,salt,salt_num,64,'sha512',(err,hashed)=>{
                        if(err){
                            console.log(err)
                            res.send({'key':0,'err_code':err.code}); //시스템 에러
                        }
                        else
                        {
                            check_password = hashed.toString('base64');
                            if(hashed_password==check_password)
                            {
                                conn.query(`update User set sign_in_status = '1' 
                                where email_address='${email_address}'`).then((data)=>{
                                    res.send({'key':3}) //로그인성공
                                }).catch((err)=>{
                                    console.log(err)
                                    res.send({'key':0,'err_code':err.code});
                                })
                            }
                            else
                            {
                                res.send({'key':4}) //로그인 실패 (패스워드 다름)
                            }
                        }
                    })
                    
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