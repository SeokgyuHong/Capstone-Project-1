
const {pool} = require('../db_connect');
const format_check = require('../format_check');
const crypto = require('crypto');
const db_loginfo = require('../secret_info/db_loginfo');


module.exports.user_type_check = (req,res)=>{
    let email_address = req.body.email_address; //이메일주소
    pool.getConnection().then((conn)=>{
        conn.query(`select user_type from User where email_address='${email_address}'`).then((data)=>{
            if(data[0]['user_type']=='normal') //일반계정이면 비밀번호 변경가능 
            {
                res.send({'key':2})//변경가능 유저타입 
            }
            else
            {
                res.send({'key':1}) //소셜계정이므로 비밀번호 변경 불가
            }
        }).catch((err)=>{
            console.log(err)
            res.send({'key':3,'err_code':err.code}); //쿼리문 에러
        })
        conn.release();
    }).catch((err)=>{
        console.log(err)
        res.send({'key':0,'err_code':err.code});
    })
}

module.exports.password_modification = (req,res)=>{
    let email_address = req.body.email_address; //이메일주소
    let old_password = req.body.old_password; //원래비밀번호
    let new_password = req.body.new_password; //변경비밀번호
    let salt = ''
    let hashed_password = ''
    let salt_num = db_loginfo.salt_num();
    if(old_password==new_password)
    {
        console.log('변경 비밀번호가 다르지않기에 종료');
        res.send({'key':3})//두 비밀번호 일치
        return;
    }
    pool.getConnection().then((conn)=>{
        conn.query(`select password, salt from User where email_address='${email_address}'`).then((data)=>{
            salt=data[0]['salt']
            crypto.pbkdf2(old_password, salt, salt_num, 64, 'sha512', (err, hashed)=> {
                if(err)
                {
                    console.log(err);
                } 
                else
                {
                    hashed_password=hashed.toString('base64');
                    if(hashed_password==data[0]['password']) //동일하다면
                    {
                        //console.log('잘되는중')
                        crypto.randomBytes(64,(err,buffer)=>{
                            crypto.pbkdf2(new_password, buffer.toString('base64'), salt_num, 64, 'sha512', (err, hashed2)=> {
                                if(err)
                                {
                                    console.log(err);
                                } 
                                else
                                {
                                    hashed_password=hashed2.toString('base64'); //변경된 비밀번호
                                    salt=buffer.toString('base64') //변경된 솔트정보
                                    console.log('새로운정보들')
                                    console.log(hashed_password)
                                    console.log(salt)
                                    conn.query(`update User set password='${hashed_password}',salt='${salt}'
                                    where email_address='${email_address}'`).then((data)=>{
                                        res.send({'key':1})//비밀번호 변경 성공
                                    }).catch((err)=>{
                                        res.send({'key':2,'err_code':err.code})//비밀번호 변경 실패
                                    })
                                
                                }
                            });
                        })
                    }
                    
                }
            });

        }).catch((err)=>{
            console.log(err)
            res.send({'key':0,'err_code':err.code})//기존패스워드 가져오기 실패
        })
    }).catch((err)=>{
        console.log(err)
        res.send({'key':0,'err_code':err.code})//시스템 에러
    })
}