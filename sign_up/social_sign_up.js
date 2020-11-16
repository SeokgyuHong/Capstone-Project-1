//프로젝트 실사용 social 로그인
const {insert} = require('../db_sql.js');
const {pool} = require('../secret_info/db_connect');
const info = require('../secret_info/db_loginfo')
const {send_mail} = require('../send_mail');
const format_check = require('../format_check');
const nodemailer = require('nodemailer');
const crypto = require('crypto');
const { resolveNaptr } = require('dns');




// module.exports.social_sign_up = (req,res)=>{
//     let email_address = req.body.email_address //이메일주소
//     let inst_name = req.body.inst_name //기관이름
//     let inst_address = req.body.inst_address //기관정보
//     let phone_number = req.body.phone_number //휴대폰번호
//     let user_type = req.body.user_type //소셜 계정 타입


//     if(!format_check.e_mail_check(email_address))
//     {
//         console.log('social_sign_up error')
//         res.send({'key':1})
//         return;
//     }
//     if(!format_check.phone_check(phone_number)) //휴대폰 번호 양식 체크
//     {
//         console.log('social_sign_up error')
//         res.send({'key':2}) //휴대폰번호 양식 에러
//         return;
//     }
//     pool.getConnection().then((conn)=>{
//         conn.query(`insert into User (email_address, inst_name, inst_address, phone_number, user_type)
//         values('${email_address}','${inst_name}','${inst_address}','${phone_number}','${user_type}')`).then((data)=>{
//             console.log('소셜 회원가입 성공')
            
//             res.send({'key':3}) // 회원가입성공
//         }).catch((err)=>{
//             console.log(err)
//             console.log('social_sign_up error')
//             res.send({'key':4,'err_code':err.code})//회원가입실패
//         })
//         conn.release();
//     }).catch((err)=>{
//         console.log(err.code)
//         console.log('social_sign_up error')
//         res.send({'key':0,'err_code':err.code}) //시스템에러
//     })
// }

module.exports.social_sign_up = (req,res)=>{
    let email_address = req.body.email_address;//이메일주소
    let user_type = req.body.user_type;
    if(!format_check.e_mail_check(email_address))
    {
        console.log('social_sign_up error')
        res.send({'key':1}) //이메일 양식 체크
        return;
    }
    pool.getConnection().then((conn)=>{
        conn.query(`select email_address,user_type from User
        where email_address='${email_address}'`).then((data)=>{
            if(data[0]===undefined) //등록된것이없으면 아무것도없다면
            {
                conn.query(`insert into User (email_address,sign_in_status,user_type)
                values('${email_address}','1','${user_type}')`).then((data)=>{
                    console.log('social sign_up_ success');
                    res.send({'key':2}) //회원가입성공 추가정보 기입란
                }).catch((err)=>{
                    console.log('err')
                    console.log('social sign up id test error');
                    console.log(err)
                    console.log("뭔에러???")
                    res.send({'key':0,'err_code':err.code});
                })
            }
            else if(data[0]['email_address']!=null && data[0]['user_type']!=user_type)
            {
                res.send({'key':3})//회원가입실패 (이메일중복)
            }
            else //로그인성공
            {
                conn.query(`update User set sign_in_status = '1' 
                where email_address='${email_address}'`).then((data)=>{
                    res.send({'key':4})//로그인성공
                }).catch((err)=>{
                    console.log('social sign up id test error');
                    console.log(err.code)
                    res.send({'key':0,'err_code':err.code});
                })

            }
        }).catch((err)=>{
            console.log('social sign up id test error');
            console.log(err.code)
            res.send({'key':0,'err_code':err.code});
        })
        conn.release();
    }).catch((err)=>{
        console.log('social sign up id test error');
        console.log(err.code)
        res.send({'key':0,'err_code':err.code});
    })
}