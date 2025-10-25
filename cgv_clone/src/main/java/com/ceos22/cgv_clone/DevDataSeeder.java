//package com.ceos22.cgv_clone;
//
//import com.ceos22.cgv_clone.domain.reservationMovie.Movie;
//import com.ceos22.cgv_clone.repository.MovieRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DevDataSeeder implements CommandLineRunner {
//
//    private final MovieRepository movieRepository;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        if (movieRepository.count() > 0) return; // 중복 삽입 방지
//
//        List<Movie> movies = List.of(
//                new Movie("서울의 봄", 140, "1979년 12월, 계엄 하의 정치 격변 속 실제 사건을 모티브로 한 드라마."),
//                new Movie("오펜하이머", 180, "핵개발 프로젝트의 딜레마와 인물의 내면을 그린 전기 드라마."),
//                new Movie("듄: 파트2", 166, "사막 행성 아라키스에서 펼쳐지는 권력과 예언의 서사."),
//                new Movie("인사이드 아웃 2", 96, "사춘기 감정들이 늘어나며 벌어지는 좌충우돌 성장기."),
//                new Movie("콘크리트 유토피아", 130, "재난 이후 한 아파트에 모인 생존자들의 이야기."),
//                new Movie("범죄도시 4", 109, "형사 마석도의 거침없는 범죄 소탕 작전."),
//                new Movie("탑건: 매버릭", 131, "전설의 파일럿이 귀환해 새로운 팀을 이끄는 항공 액션."),
//                new Movie("어벤져스: 엔드게임", 181, "인피니티 사가의 대미를 장식하는 히어로 대서사."),
//                new Movie("라라랜드", 128, "꿈을 좇는 두 청춘의 사랑과 선택을 담은 뮤지컬 드라마."),
//                new Movie("기생충", 132, "두 가족의 얽힘으로 드러나는 계급의 아이러니."),
//                new Movie("인터스텔라", 169, "인류의 미래를 위한 우주 탐사와 시간의 상대성."),
//                new Movie("소울", 100, "음악가의 영혼 여행을 통해 삶의 의미를 묻는 애니메이션.")
//        );
//
//        movieRepository.saveAll(movies);
//    }
//}
