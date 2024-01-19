package org.choongang.board.entites;

import org.choongang.member.entities.Member;

public interface AuthCheck {
    boolean isEditable();
    boolean isDeletable();
    boolean isMine();

    Member getMember();

}
