package com.medibook.config;

import com.medibook.domain.department.Department;
import com.medibook.domain.department.DepartmentRepository;
import com.medibook.domain.doctor.Doctor;
import com.medibook.domain.doctor.DoctorRepository;
import com.medibook.domain.user.User;
import com.medibook.domain.user.UserRepository;
import com.medibook.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 애플리케이션 최초 실행 시 진료과 + 관리자 + 의사 계정을 일괄 생성합니다.
 * 이미 데이터가 있으면 스킵합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String STAFF_PASSWORD = "Doctor123!";
    private static final String PATIENT_PASSWORD = "Test1234!";

    @Override
    @Transactional
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            log.info("✅ 초기 데이터가 이미 존재합니다. 스킵합니다.");
            return;
        }

        log.info("🏥 MediBook 초기 데이터 생성 시작...");

        // 1. 진료과 생성
        List<Department> departments = createDepartments();
        log.info("   ✓ 진료과 {}개 생성", departments.size());

        // 2. 관리자 생성
        createAdmin();
        log.info("   ✓ 관리자 계정 생성 (admin@medibook.kr)");

        // 3. 의사 생성
        createDoctors(departments);
        log.info("   ✓ 의사 계정 9개 생성");

        // 4. 테스트 환자 생성
        createTestPatient();
        log.info("   ✓ 테스트 환자 계정 생성 (hong@test.com)");

        log.info("🎉 초기 데이터 생성 완료!");
        log.info("========================================");
        log.info("  관리자: admin@medibook.kr / Doctor123!");
        log.info("  의  사: kim.jh@medibook.kr / Doctor123!");
        log.info("  환  자: hong@test.com / Test1234!");
        log.info("========================================");
    }

    private List<Department> createDepartments() {
        List<Department> depts = List.of(
                Department.builder().name("내과").description("감기, 소화기질환, 호흡기질환 등 내과 전반 진료").icon("🫀").build(),
                Department.builder().name("소아과").description("영유아 및 소아 질환 전문 진료").icon("👶").build(),
                Department.builder().name("정형외과").description("근골격계 질환, 스포츠 의학 전문").icon("🦴").build(),
                Department.builder().name("피부과").description("피부질환, 미용피부 전문 진료").icon("✨").build(),
                Department.builder().name("안과").description("시력교정, 망막질환 전문 진료").icon("👁️").build(),
                Department.builder().name("치과").description("충치, 교정, 임플란트 전문").icon("🦷").build(),
                Department.builder().name("산부인과").description("여성건강, 산전관리 전문 진료").icon("🤰").build(),
                Department.builder().name("외과").description("일반외과, 수술 전문 진료").icon("⚕️").build()
        );
        return departmentRepository.saveAll(depts);
    }

    private void createAdmin() {
        User admin = User.builder()
                .name("시스템관리자")
                .email("admin@medibook.kr")
                .password(passwordEncoder.encode(STAFF_PASSWORD))
                .phone("02-1234-5678")
                .birthdate("1980-01-01")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin);
    }

    private void createDoctors(List<Department> departments) {
        // [이름, 이메일, 전화, 생년월일, 진료과인덱스, 전문분야, 대학, 소개, 경력, 태그, 경력연수, 평점, 리뷰수]
        Object[][] doctorData = {
                {"김정현", "kim.jh@medibook.kr", "010-1001-0001", "1978-03-15", 0,
                        "소화기내과 전문의", "서울대학교 의과대학",
                        "소화기 질환 분야의 풍부한 임상 경험을 바탕으로 환자 중심의 정밀 진료를 제공합니다.",
                        "서울대병원 내과 전공의 수료, 삼성서울병원 소화기내과 펠로우",
                        "소화기,위내시경,대장내시경,역류성식도염", 15, 4.9, 342},

                {"이수민", "lee.sm@medibook.kr", "010-1001-0002", "1982-07-22", 1,
                        "소아 알레르기 전문의", "연세대학교 의과대학",
                        "아이들의 건강한 성장을 돕는 소아 알레르기 전문의입니다.",
                        "세브란스병원 소아과 전공의, 국립어린이의료센터 알레르기 펠로우",
                        "소아알레르기,아토피,소아천식,영유아검진", 12, 4.8, 287},

                {"박현우", "park.hw@medibook.kr", "010-1001-0003", "1975-11-08", 2,
                        "스포츠 의학 전문의", "고려대학교 의과대학",
                        "스포츠 손상부터 퇴행성 관절질환까지 폭넓은 정형외과 진료를 합니다.",
                        "아산병원 정형외과 전공의, 국가대표 팀닥터 역임",
                        "스포츠의학,관절,인대손상,재활", 18, 4.9, 198},

                {"최은지", "choi.ej@medibook.kr", "010-1001-0004", "1985-05-30", 3,
                        "미용 피부 전문의", "이화여자대학교 의과대학",
                        "최신 피부 레이저 기술과 맞춤형 스킨케어를 제공합니다.",
                        "강남세브란스 피부과 전공의, 대한피부과학회 정회원",
                        "미용피부,레이저,여드름,피부재생", 10, 4.7, 156},

                {"정민호", "jung.mh@medibook.kr", "010-1001-0005", "1980-09-12", 0,
                        "호흡기내과 전문의", "성균관대학교 의과대학",
                        "천식, 폐렴 등 호흡기 질환의 정확한 진단과 치료를 제공합니다.",
                        "삼성서울병원 호흡기내과 전공의, 대한결핵학회 정회원",
                        "호흡기,천식,폐렴,기관지염", 13, 4.8, 231},

                {"한서윤", "han.sy@medibook.kr", "010-1001-0006", "1979-01-25", 4,
                        "망막 전문의", "중앙대학교 의과대학",
                        "당뇨망막병증, 황반변성 등 망막질환 전문 진료를 합니다.",
                        "서울아산병원 안과 전공의, 하버드 의대 망막 연수",
                        "망막,황반변성,당뇨망막,시력교정", 16, 4.9, 178},

                {"윤재호", "yoon.jh@medibook.kr", "010-1001-0007", "1983-06-18", 5,
                        "교정 전문의", "경희대학교 치과대학",
                        "투명교정부터 일반교정까지 아름다운 미소를 만들어드립니다.",
                        "서울대치과병원 교정과 전공의, 대한교정치과학회 인정의",
                        "치아교정,투명교정,소아교정,턱교정", 11, 4.6, 203},

                {"강미래", "kang.mr@medibook.kr", "010-1001-0008", "1981-12-05", 6,
                        "산과 전문의", "가톨릭대학교 의과대학",
                        "안전한 출산과 여성 건강을 위한 맞춤 진료를 제공합니다.",
                        "서울성모병원 산부인과 전공의, 대한모체태아의학회 정회원",
                        "산전관리,분만,여성건강,고위험임신", 14, 4.8, 145},

                {"오성민", "oh.sm@medibook.kr", "010-1001-0009", "1976-04-20", 7,
                        "일반외과 전문의", "울산대학교 의과대학",
                        "복강경 수술 분야의 전문가로 최소 침습 수술을 지향합니다.",
                        "서울아산병원 외과 전공의, 복강경수술 인증의",
                        "일반외과,복강경,탈장,갑상선", 17, 4.7, 189}
        };

        for (Object[] d : doctorData) {
            // User 생성
            User user = User.builder()
                    .name((String) d[0])
                    .email((String) d[1])
                    .password(passwordEncoder.encode(STAFF_PASSWORD))
                    .phone((String) d[2])
                    .birthdate((String) d[3])
                    .role(UserRole.DOCTOR)
                    .build();
            user = userRepository.save(user);

            // Doctor 프로필 생성
            Doctor doctor = Doctor.builder()
                    .user(user)
                    .department(departments.get((int) d[4]))
                    .specialty((String) d[5])
                    .education((String) d[6])
                    .bio((String) d[7])
                    .career((String) d[8])
                    .tags((String) d[9])
                    .experienceYears((int) d[10])
                    .rating((double) d[11])
                    .reviewCount((int) d[12])
                    .build();
            doctorRepository.save(doctor);
        }
    }

    private void createTestPatient() {
        User patient = User.builder()
                .name("홍길동")
                .email("hong@test.com")
                .password(passwordEncoder.encode(PATIENT_PASSWORD))
                .phone("010-1234-5678")
                .birthdate("1990-06-15")
                .role(UserRole.PATIENT)
                .build();
        userRepository.save(patient);
    }
}


