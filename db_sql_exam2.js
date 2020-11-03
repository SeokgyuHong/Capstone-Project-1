// 10월 28일 과제 
const {pool} = require('./db_connect');
function e_mail_check(e_mail){ //이메일 정규식 체크 함수
    let e_mail_check = /^[0-9a-zA-z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;     
    if(!e_mail_check.test(e_mail))
    {
        return false;
    }
    else
    {
        return true;
    }
}
function phone_check(phone_number){ //휴대폰 번호 및 일반전화 정규식 체크 함수
    let phone_number_check_1 = /^[0-9]{3}[-]+[0-9]{4}[-]+[0-9]{4}$/;     //휴대전화
    let phone_number_check_2 = /^\d{2,3}-\d{3,4}-\d{4}$/;  //일반전화 
    if(!phone_number_check_1.test(phone_number)&&!phone_number_check_2.test(phone_number)) //일반전화번호와 휴대폰번호 둘다 양식에서 어긋날경우
    {
        return false;
    }
    else
    {
        return true;
    }

}
function company_check(company_number){ //사업자번호 정규식 체크 함수
    let company_number_check = /^\d{3}-\d{2}-\d{3}$/;
    if(!company_number_check.test(company_number))
    {
        return false;
    }
    else
    {
        return true;
    }
}

//기관정보 요청 함수 
module.exports.inst_req = (req,res)=>{
    let id = req.body.id; //조회하고자 하는 기관 아이디
    
    pool.getConnection().then((conn)=>{
        conn.query(`select * from institution where id = ${id}`).then((data)=>{
            if(data[0]==undefined){ //테이블상에 해당 id가 없다면
                console.log("존재 하지 않는 id입니다.")
                res.send("존재 하지 않는 id입니다.")

            }
            else{
                console.log(data[0])
                res.send(data[0]); //쿼리에서 받아온 해당 기관 정보 객체 전달
            }
        }).catch((err)=>{
            console.log(err)
            res.send(err.code); //에러정보 전송 
        })
        conn.release(); //쿼리 객체 release
    }).catch((err)=>{
        console.log(err)
        res.send(err) //에러정보 전송
    })
 
}
module.exports.inst_insert = (req,res)=>{
    let inst_id = req.body.id;//기관 id
    let inst_name = req.body.name;//기관이름
    let inst_phone = req.body.phone;//기관 휴대폰번호
    let rep_name = req.body.rep_name; //대표자 이름
    let rep_phone = req.body.rep_phone; //대표자 휴대폰번호
    let rep_email = req.body.rep_email; //대표자 이메일
    let rep_password = req.body.rep_password; //대표자 비밀번호
    let business_num = req.body.business_num; //사업자 번호
    if(!e_mail_check(rep_email))
    {
        res.status(400).send({'key':1}) //이메일 양식 에러
        
    }
    if(!phone_check(inst_phone))
    {
        res.status(400).send({'key':2})//기관번호 양식 에러
    }
    if(!phone_check(rep_phone))
    {
        res.status(400).send({'key':3})//대표자번호 양식 에러
    }
    if(!company_check(business_num))
    {
        res.status(400).send({'key':4}) //사업자번호 양식 에러
    }
    
    pool.getConnection().then((conn)=>{
        conn.query(`insert into institution (institution_id,inst_name,inst_phone,activation,rep_name,rep_phone,rep_email,rep_password,business_num)values(${inst_id},'${inst_name}','${inst_phone}','N','${rep_name}','${rep_phone}','${rep_email}','${rep_password}','${business_num}')`).then((data)=>{
            console.log(data);
            res.status(200).send({'key':5}) //데이터 삽입 성공
        }).catch((err)=>{
            console.log(err);
            res.status(400).send({'key':err.code}); //에러코드 전송
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code);
        res.status(400).send({'key':err.code}); //에러코드 전송
    })


    
}
//기관정보수정
module.exports.inst_modify = (req,res)=>{
    let id = req.body.id; //매니저 아이디
    let name = req.body.name; //담당자이름
    let phone_number = req.body.phone_number; //휴대폰번호
    let e_mail = req.body.e_mail; //이메일정보
    let pw = req.body.pw; //비밀번호

   //이메일 정규식

    if(!e_mail_check(e_mail))
    {
        console.log("이메일 양식 에러");
        res.status(400).send({'key':1}) //이메일양식에러
            
    }

    if(!phone_check(phone_number))
    {
        console.log("번호 양식 에러");
        res.status(400).send({'key':3}) //담당자번호에러
    }

        pool.getConnection().then((conn)=>{
            conn.query(`update institution join manager on institution.rep_name=manager.nameset institution.rep_phone='${phone_number}', institution.rep_name='${name}', institution.rep_email='${e_mail}', institution.rep_password='${pw}}',manager.phone_number='${phone_number}', manager.name='${name}', manager.email='${e_mail}', manager.password='${pw}'where manager.manager_id = ${id};`).then((data)=>{
                console.log(data);
                res.status(200).send({'key':6}) //데이터 수정 성공
            }).catch((err)=>{
                console.log(err.code);
                res.status(400).send({'key':err.code}); //에러코드 전송
            })
            conn.release();
        }).catch((err)=>{
            console.log(err.code);
            res.status(400).send({'key':err.code}); //에러코드전송
            })

    
    
}


module.exports.inst_delete = (req,res)=>{ //기관삭제
    let id = req.body.id; //기관 아이디

    pool.getConnection().then((conn)=>{
        conn.query(`delete from institution where institution_id = ${id}`).then((data)=>{
            console.log(data);
            res.status(200).send({'key':7}) //데이터 삭제 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(400).send({'key':err.code}); //에러원인 전송
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code);
        res.status(400).send({'key':err.code});
    })

}


module.exports.rep_req = (req,res)=>{ //담당자 조회
    let manager_id = req.body.manager_id; //담당자 id 
    
    pool.getConnection().then((conn)=>{
        conn.query(`select * from manager where manager_id = ${manager_id}`).then((data)=>{
            console.log(data[0]);
            res.send(data[0]); //담당자 정보 전달
        }).catch((err)=>{
            console.log(err.code);
            res.status(400).send({'key':err.code});
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code);
        res.status(400).send({'key':err.code});
    })
}

module.exports.rep_insert = (req,res)=>{
    let manager_id = req.body.manager_id; //담당자 아이디
    let inst_id = req.body.inst_id; //기관 아이디
    let name = req.body.name; //담당자이름
    let department = req.body.department; //담당자 부서
    let working_status = req.body.working_status; //근무현황
    let phone_number= req.body.phone_number; //휴대폰번호
    let e_mail = req.body.e_mail; //이메일정보
    let password = req.body.password; //비밀번호
    let login_count = rqe.body.login_count; //로그인횟수

    
    if(!e_mail_check(e_mail))
    {
        console.log("이메일 양식 에러");
        res.status(400).send({'key':1}) //이메일양식에러
            
    }
    if(!phone_check(phone_number))
    {
        console.log("번호 양식 에러");
        res.status(400).send({'key':3}) //담당자번호에러
    }
    
    pool.getConnection().then((conn)=>{
        conn.query(`insert into manager (manager_id,inst_id,name,department,working_status,phone_number,email,password,login_count) values (${manager_id},${inst_id},'${name}','${department}','${working_status}','${phone_number}','${e_mail}','${password}',${login_count})`).then((data)=>{
            console.log(data)
            res.status(200).send({'key':8}) //담당자 데이터 삽입 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(400).send({'key':err.code});
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code);
        res.status(400).send({'key':err.code});
    })
}

module.exports.rep_modify = (req,res)=>{
    let manager_id = req.body.manager_id; //담당자 아이디
    let name = req.body.name; //담당자이름
    let phone_number= req.body.phone_number; //휴대폰번호
    let e_mail = req.body.e_mail; //이메일정보
    let password = req.body.password; //비밀번호
 
   
    if(!e_mail_check(e_mail))
    {
        console.log("이메일 양식 에러");
        res.status(400).send({'key':1}) //이메일양식에러
            
    }
    if(!phone_check(phone_number))
    {
        console.log("번호 양식 에러");
        res.status(400).send({'key':3}) //담당자번호에러
    }

        pool.getConnection().then((conn)=>{
            conn.query(`update manager set manager.name='${name}',manager.phone_number='${phone_number}',manager.email='${e_mail}',manager.password='${password}' where manager.id=${manager_id}`).then((data)=>{
                console.log(data);
                res.status(200).send({'key':9}) //데이터 수정 성공
            }).catch((err)=>{
                console.log(err.code);
                res.status(400).send({'key':err.code}); //에러코드 전송
            })
            conn.release();
        }).catch((err)=>{
            console.log(err.code);
            res.status(400).send({'key':err.code}); //에러코드전송
        })

    
    
}

module.exports.rep_delete = (req,res)=>{ //담당자 삭제
    let manager_id = req.body.manager_id; //담당자 아이디

    pool.getConnection().then((conn)=>{
        conn.query(`delete from manager where manager_id = ${manager_id}`).then((data)=>{
            console.log(data);
            res.status(200).send({'key':10}) //데이터 삭제 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(400).send({'key':err.code}); //에러원인 전송
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code);
        res.status(400).send({'key':err.code});
    })

}
