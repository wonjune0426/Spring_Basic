package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Component
// @RequiredArgsConstructor - lombok을 통한 생성자 주입
@Service // 3 layer Annotation
public class MemoService {

    private final MemoRepository memoRepository;

//    public MemoService(ApplicationContext context) {
//         1. 'Bean' 이름으로 가져오기
//        MemoRepository memoRepository = (MemoRepository)context.getBean("memoRepository");
//        this.memoRepository = memoRepository;
//
//         2, 'Bean' 클래스 형식으로 가져오기
//        MemoRepository memoRepository = (MemoRepository)context.getBean(MemoRepository.class);
//        this.memoRepository = memoRepository;
//    }

    // @Autowired
    // DI Annotation
    // 생성자 하나일 경우 Spring 4.3부터 삭제 가능
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAll().stream().map(MemoResponseDto::new).toList();
    }

    @Transactional
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        memo.update(requestDto);
        return id;
    }

    public Long deleteMemo(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // memo 삭제
        memoRepository.delete(memo);

        return id;

    }

    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }
}
