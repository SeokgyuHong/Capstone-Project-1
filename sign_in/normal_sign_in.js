const {log_info} = require('../secret_info/db_loginfo.js');
const {insert} = require('../db_sql.js');
const {pool} = require('../db_connect');
const info = require('../secret_info/db_loginfo')
const format_check = require('../format_check');
const crypto = require('crypto');
const { resolveNaptr } = require('dns');




module.exports.normal_sign_in =(req,res)=>{
    let email_address = req.body.email_address;
    let password = req.body.password;
    let salt=''
    let hashed_password=''
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
                conn.query(`select salt from User where email_address='${email_address}'`).then((data)=>{
                    salt = data[0]['salt'];
                    crypto.pbkdf2(password,salt,130495,64,'sha512',(err,hashed)=>{
                        if(err){
                            console.log(err);
                            //console.log('asdfsadf')
                            res.send({'key':0,'err_code':err.code}); //시스템에러
                        }
                        else
                        {
                            hashed_password=hashed;
                            conn.query(`select password from User where email_address='${email_address}'`).then((data)=>{
                                if(data[0]['password']==hashed_password)
                                {
                                    conn.query(`update User set sign_in_status = 1 where email_address='${email_address}'`).then((data)=>{
                                        res.send({'key':3}) // 로그인성공
                                    }).catch((err)=>{
                                        console.log(err);
                                        //console.log('asdfadsf')
                                        res.send({'key':0,'err_code':err.code}); //시스템에러
                                    })
                                }
                                else
                                {
                                    res.send({'key':4}) //로그인실패(비밀번호 다름)
                                }
                            }).catch((err)=>{
                                console.log(err);
                                //console.log('adsfsadf')
                                res.send({'key':0,'err_code':err.code})
                            })
                        }
                    })
                }).catch((err)=>{
                    console.log(err);
                    res.send({'key':0,'err_code':err.code});
                })
            }
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code);
        res.send({'key':0,'err_code':err.code});
    })


    // crypto.randomBytes(64,(err,buffer)=>{
    //     crypto.pbkdf2(password, buffer.toString('base64'), 130495, 64, 'sha512', (err, hashed)=> {
    //         if(err){
    //         console.log(err);
    //         } else{
    //         hashed_password=hashed.toString('base64');
    //         salt=buffer.toString('base64')
    //         console.log(hashed_password);
    //         }
    //     });
    // })
    

}