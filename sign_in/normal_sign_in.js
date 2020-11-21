//프로젝트 실사용 로그인 
//일반회원가입 구현 완료
//const {insert} = require('../db_sql.js');
const {pool} = require('../secret_info/db_connect');
const info = require('../secret_info/db_loginfo')
const {send_mail} = require('../send_mail');
const format_check = require('../format_check');
const nodemailer = require('nodemailer');
const crypto = require('crypto');
module.exports.id_duplication_check = (req,res)=>{
    let email_address = req.body.email_address

    if(!format_check.e_mail_check(email_address)) //이메일 양식 체크
    {
        res.send({'key':1}) //이메일 양식 에러
        return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`select email_address from User where email_address='${email_address}'`).then((data)=>{ //아이디 중복여부 체크 쿼리
            if(data[0] ===undefined) 
            {
               res.send({'key':2}) //이메일 중복 x
            }
            else //이메일이 존재할경우
            {
                res.send({'key':3}) //이메일 중복 있음
            }
        }).catch((err)=>{
            console.log(err.code)
            res.send({'key':0,'err_code':err.code}) // 시스템에러
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code)
        res.send({'key':0,'err_code':err.code}) //0 은 시스템 에러 
    })
}


module.exports.temp_pw_create = (req,res)=>{
    let email_address = req.body.email_address //이메일주소
    let mail_info = info.mail_config(); // 전송 이메일 정보
    let mail_option = send_mail(email_address);
    let mailOption;
    let temp_password;
    
    mailOption=mail_option.mailOption; //메일전송객체
    temp_password = mail_option.temp_pw; //임시비밀번호


    if(!format_check.e_mail_check(email_address)) //이메일 양식 체크
    {
        res.send({'key':1}) //이메일 양식 에러
        return;
    }
    let transporter = nodemailer.createTransport({ //메일전송객체 생성
        service:'gmail',
        auth:{
            user:mail_info.id,
            pass:mail_info.pw
        }
    });
    transporter.sendMail(mailOption).then((info)=>{ //메일전송
        console.log('Email sent: ' + info.response);
        pool.getConnection().then((conn)=>{
            conn.query(`select email_address from Temp_user 
            where email_address='${email_address}'`).then((data)=>{
                if(data[0]===undefined)
                {
                conn.query(`insert into Temp_user (email_address,temp_password)
                values('${email_address}','${temp_password}')`).then((data)=>{
                    res.send({'key':3}) // 임시비밀번호 생성완료
                }).catch((err)=>{
                    console.log('임시비밀번호생성시에러')
                    console.log(err.code)
                    res.send({'key':0,'err_code':err.code});
                })
                }
                else
                {
                conn.query(`update Temp_user set temp_password = '${temp_password}' 
                where email_address='${email_address}'`).then((data)=>{
                    res.send({'key':3}) // 임시비밀번호 생성완료
                }).catch((err)=>{
                    console.log('임시비밀번호생성시에러')
                    console.log(err.code)
                    res.send({'key':0,'err_code':err.code});
                })
                }
            conn.release();
             }).catch((err)=>{
                console.log('임시비밀번호생성시에러')
                console.log(err.code)
                res.send({'key':0,'err_code':err.code});
            })
    })
    }).catch((err)=>{
        if(err)
        {
            console.log(err)
            console.log('이메일 발송부 에러')
            res.send({'key':2}) //임시비밀번호 생성 및 이메일 처리 오류
        }
    })

}


module.exports.temp_pw_check = (req,res)=>{ 
    let email_address = req.body.email_address //이메일주소
    let temp_password= req.body.temp_password;

    pool.getConnection().then((conn)=>{
        conn.query(`select temp_password from Temp_user where email_address='${email_address}'`).then((data)=>{
            if (data[0]['temp_password']==temp_password)
            {
                res.send({'key':1})//임시비밀번호 일치
            }
            else
            {
                res.send({'key':2}) // 임시비밀번호가 다름
            }
        }).catch((err)=>{
            console.log(err.code)
            res.send({'key':0,'err_code':err.code}) //0 은 시스템 에러 
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code)
        res.send({'key':0,'err_code':err.code}) //0 은 시스템 에러 
    })
}

module.exports.normal_sign_up = (req,res)=>{
    let email_address = req.body.email_address //이메일주소
    let inst_name = req.body.inst_name // 기관이름
    let inst_address = req.body.inst_address //기관주소
    let password = req.body.password // 비밀번호
    let phone_number = req.body.phone_number //휴대폰 번호
    let hashed_password=''
    let salt_num = info.salt_num();

    if(!format_check.phone_check(phone_number)) //휴대폰번호 양식 체크
    {
        res.send({'key':2}) //휴대폰번호 양식 에러
        return;
    }
    let salt=''
    crypto.randomBytes(64,(err,buffer)=>{
        crypto.pbkdf2(password, buffer.toString('base64'), salt_num, 64, 'sha512', (err, hashed)=> {
            if(err){
            console.log(err);
            } else{
            hashed_password=hashed.toString('base64');
            salt=buffer.toString('base64')
            console.log(hashed_password);
            }
        });
    })
    pool.getConnection().then((conn)=>{
        conn.query(`delete from Temp_user where email_address ='${email_address}'`).then((data)=>{
            console.log('임시 비밀번호 데이터 삭제 성공')
            conn.query(`insert into User (email_address,phone_number,password,salt,user_type)
            values('${email_address}','${phone_number}','${hashed_password}','${salt}','normal')`).then((data)=>{
                console.log('회원가입 성공')
                res.send({'key':3}) //회원가입성공
            }).catch((err)=>{
                console.log(err)
                res.send({'key':4,'err_code':err.code})// 회원가입실패
            })
        
        }).catch((err)=>{
            console.log('시스템 에러 임시비밀번호 할당 삭제 시 에러발생')
            res.send({'key':0,'err_code':err.code}); //시스템에러
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code)
        res.send({'key':0,'err_code':err.code}) //시스템에러
    })

}