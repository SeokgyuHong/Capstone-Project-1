// 11월 4일 과제
const {pool} = require('./db_connect');

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

function sex_check(sex){
    if(sex !='M' && sex!='F')
    {
        return false;
    }
    else
    {
        return true;
    }
}
//보호자 정보 요청
module.exports.guardian_info_request = (req,res)=>{
    let id = req.body.id; //보호자 id
    pool.getConnection().then((conn)=>{
        conn.query(`select * from ${테이블명} where id = ${id}`).then((data)=>{
            console.log(data[0])
            res.status(200).send(data[0])
        }).catch((err)=>{
            console.log(err);//에러원인출력
            res.status(200).send({'key':2,'err_code':err_code}) //에러원인전송 및 에러코드 3번지정

        })
        conn.release()
    }).catch((err)=>{
        console.log(err);
        res.status(200).send({'key':0}) //시스템 내부 오류 0번 지정
    })
}
//보호자 정보 삽입
module.exports.gurdian_info_insert= (req,res)=>{
    let id = req.body.id;
    let name = req.body.name;
    let number = req.body.number;
    let patient_id = req.body.patient_id;
    
    if(!phone_check(number))
    {
        res.status(200).send({'key':1})//휴대폰 번호 또는 일반 번호 양식 오류
    }
    
    pool.getConnection().then((conn)=>{
        conn.query(`insert into ${테이블명} set() values()`).then((data)=>{
            res.status(200).send({'key':7}) //정보 삽입 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(200).send({'key':3,'err_code':err_code}) //key 보호자 정보 삽입 에러
        })
        conn.release()
    }).catch((err)=>{
        console.log(err);
        res.status(200).send({'key':0}) //시스템 내부 오류 0번 지정
    })
}

//보호자 정보 수정 #일괄 수정 전제
module.exports.gurdian_info_modification= (req,res)=>{
    let id = req.body.id;
    let name = req.body.name;
    let number = req.body.number;
    let patient_id = req.body.patient_id;
    
    if(!phone_check(number))
    {
        res.status(200).send({'key':1})//휴대폰 번호 또는 일반 번호 양식 오류
    }
    
    pool.getConnection().then((conn)=>{
        conn.query(`update ${테이블명} ~~ where id = ${조건}`).then((data)=>{
            res.status(200).send({'key':8}) //정보 수정 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(200).send({'key':4,'err_code':err_code}) //key 보호자 정보 수정 에러
        })
        conn.release()
    }).catch((err)=>{
        console.log(err);
        res.status(200).send({'key':0}) //시스템 내부 오류 0번 지정
    })
}
//보호자 정보 삭제
module.exports.gurdian_info_delete= (req,res)=>{
    let id = req.body.id;


    
    pool.getConnection().then((conn)=>{
        conn.query(`delete from ${테이블명} where id = ${조건}`).then((data)=>{
            res.status(200).send({'key':9}) //정보 삭제 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(200).send({'key':5,'err_code':err_code}) //key 보호자 정보 삭제 에러
        })
        conn.release()
    }).catch((err)=>{
        console.log(err);
        res.status(200).send({'key':0}) //시스템 내부 오류 0번 지정
    })
}

//환자정보

//독거인 정보 요청
module.exports.patient_info_request = (req,res)=>{
    let id = req.body.id; //독거인 id
    pool.getConnection().then((conn)=>{
        conn.query(`select * from ${테이블명} where id = ${id}`).then((data)=>{
            console.log(data[0])
            res.status(200).send(data[0])
        }).catch((err)=>{
            console.log(err);//에러원인출력
            res.status(200).send({'key':2,'err_code':err_code}) //에러원인전송 및 에러코드 3번지정

        })
        conn.release()
    }).catch((err)=>{
        console.log(err);
        res.status(200).send({'key':0}) //시스템 내부 오류 0번 지정
    })
}

//독거인 정보 삽입
module.exports.patient_info_insert= (req,res)=>{
    let id = req.body.id; //독거인아이디
    let name = req.body.name; //이름
    let dept_number = req.body.dept_number; //건물번호
    let inst_id = req.body.inst_id //기관 아이디
    let age = req.body.age;//나이
    let sex = req.body.sex;//성별
    let date = req.body.date; //생년월일

    if(!sex_check(number))
    {
        res.status(200).send({'key':6})//독거인 성별 정보 오류
    }
    
    pool.getConnection().then((conn)=>{
        conn.query(`insert into ${테이블명} set() values()`).then((data)=>{
            res.status(200).send({'key':7}) //정보 삽입 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(200).send({'key':3,'err_code':err_code}) //key 독거인 정보 삽입 에러
        })
        conn.release()
    }).catch((err)=>{
        console.log(err);
        res.status(200).send({'key':0}) //시스템 내부 오류 0번 지정
    })
}



//독거인정보 수정 #일괄 수정 전제
module.exports.gurdian_info_modification= (req,res)=>{
    let id = req.body.id; //독거인아이디
    let name = req.body.name; //이름
    let dept_number = req.body.dept_number; //건물번호
    let inst_id = req.body.inst_id //기관 아이디
    let age = req.body.age;//나이
    let sex = req.body.sex;//성별
    let date = req.body.date; //생년월일

    
    if(!sex_check(number))
    {
        res.status(200).send({'key':6})//독거인 성별 정보 오류
    }
    
    pool.getConnection().then((conn)=>{
        conn.query(`update ${테이블명} ~~ where id = ${조건}`).then((data)=>{
            res.status(200).send({'key':8}) //정보 수정 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(200).send({'key':4,'err_code':err_code}) //key 독거인 정보 수정 에러
        })
        conn.release()
    }).catch((err)=>{
        console.log(err);
        res.status(200).send({'key':0}) //시스템 내부 오류 0번 지정
    })
}
//보호자 정보 삭제
module.exports.gurdian_info_delete= (req,res)=>{
    let id = req.body.id;
    pool.getConnection().then((conn)=>{
        conn.query(`delete from ${테이블명} where id = ${조건}`).then((data)=>{
            res.status(200).send({'key':9}) //정보 삭제 성공
        }).catch((err)=>{
            console.log(err.code);
            res.status(200).send({'key':5,'err_code':err_code}) //key 독거인 정보 삭제 에러
        })
        conn.release()
    }).catch((err)=>{
        console.log(err);
        res.status(200).send({'key':0}) //시스템 내부 오류 0번 지정
    })
}