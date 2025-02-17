package app.user;

import com.fasterxml.jackson.databind.node.ObjectNode;

interface Visitor {
    ObjectNode visit(User user);
    ObjectNode visit(Artist artist);
    ObjectNode visit(Host host);
}
